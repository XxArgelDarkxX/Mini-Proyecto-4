import asyncio
import pytest
from fastapi.testclient import TestClient

from app.main import app
from app.db import mongo


class FakeInsertResult:
    def __init__(self, inserted_id):
        self.inserted_id = inserted_id


class FakeUpdateResult:
    def __init__(self, matched_count: int):
        self.matched_count = matched_count


class FakeCursor:
    def __init__(self, items):
        self._items = list(items)
        self._limit = None

    def limit(self, value: int):
        self._limit = value
        return self

    async def to_list(self, length: int):
        items = self._items
        if self._limit is not None:
            items = items[: self._limit]
        if length is not None:
            items = items[:length]
        return items


class FakeCollection:
    def __init__(self, items=None):
        self._items = list(items or [])
        self._id_seq = 1

    async def find_one(self, query):
        for item in self._items:
            if _match(item, query):
                return item.copy()
        return None

    async def insert_one(self, doc):
        stored = doc.copy()
        stored.setdefault("_id", str(self._id_seq))
        self._id_seq += 1
        self._items.append(stored)
        return FakeInsertResult(stored["_id"])

    async def update_one(self, query, update, upsert=False):
        for item in self._items:
            if _match(item, query):
                _apply_update(item, update)
                return FakeUpdateResult(1)
        if upsert:
            new_doc = {}
            if query:
                new_doc.update(query)
            _apply_update(new_doc, update)
            self._items.append(new_doc)
            return FakeUpdateResult(1)
        return FakeUpdateResult(0)

    async def find_one_and_update(self, query, update, upsert=False, return_document=True):
        for item in self._items:
            if _match(item, query):
                _apply_update(item, update)
                return item.copy()
        if upsert:
            new_doc = dict(query or {})
            _apply_update(new_doc, update)
            self._items.append(new_doc)
            return new_doc.copy()
        return None

    def find(self, query=None, projection=None):
        results = []
        for item in self._items:
            if query is None or _match(item, query):
                results.append(item.copy())
        return FakeCursor(results)


class FakeDB:
    def __init__(self):
        self.users = FakeCollection()
        self.trainers = FakeCollection()
        self.pokemons = FakeCollection()
        self.trainer_counters = FakeCollection([{"_id": "trainer_suid", "seq": 0}])


def _match(item, query):
    if not query:
        return True
    if "$or" in query:
        return any(_match(item, clause) for clause in query["$or"])
    for key, value in query.items():
        if key == "$or":
            continue
        if isinstance(value, dict) and "$or" in value:
            if any(_match(item, clause) for clause in value["$or"]):
                continue
            return False
        if isinstance(value, dict) and "$regex" in value:
            target = str(item.get(key, ""))
            pattern = value["$regex"]
            options = value.get("$options", "")
            flags = 0
            if "i" in options:
                flags |= __import__("re").IGNORECASE
            if __import__("re").match(pattern, target, flags) is None:
                return False
            continue
        if item.get(key) != value:
            return False
    return True


def _apply_update(item, update):
    if not update:
        return
    if "$set" in update:
        for key, value in update["$set"].items():
            item[key] = value
    if "$inc" in update:
        for key, value in update["$inc"].items():
            item[key] = item.get(key, 0) + value


@pytest.fixture
def client():
    fake_db = FakeDB()
    mongo.db = fake_db
    app.router.on_startup = []
    app.router.on_shutdown = []
    with TestClient(app) as test_client:
        yield test_client, fake_db
    mongo.db = None


def _auth_headers(client):
    response = client.post("/api/auth/register", json={"username": "user1", "password": "pass123"})
    token = response.json()["access_token"]
    return {"Authorization": f"Bearer {token}"}


def run_async(coro):
    return asyncio.run(coro)


def test_health(client):
    test_client, _ = client
    response = test_client.get("/health")
    assert response.status_code == 200
    assert response.json() == {"status": "ok"}


def test_register_and_login(client):
    test_client, fake_db = client
    response = test_client.post("/api/auth/register", json={"username": "user2", "password": "pass123"})
    assert response.status_code == 200
    assert "access_token" in response.json()

    login = test_client.post("/api/auth/login", json={"username": "user2", "password": "pass123"})
    assert login.status_code == 200
    assert "access_token" in login.json()

    user = run_async(fake_db.users.find_one({"username": "user2"}))
    assert user is not None


def test_create_trainer_and_search(client):
    test_client, fake_db = client
    headers = _auth_headers(test_client)

    response = test_client.post("/api/trainers/", json={"name": "Angel"}, headers=headers)
    assert response.status_code == 200
    suid = response.json()["suid"]

    search = test_client.get("/api/trainers/search?name=Angel", headers=headers)
    assert search.status_code == 200
    assert search.json()["suid"] == suid


def test_team_and_stats_endpoints(client):
    test_client, fake_db = client
    headers = _auth_headers(test_client)

    pokemon_doc = {
        "pokedex_id": "1",
        "name": "bulbasaur",
        "type": "GRASS",
        "experience": 0,
        "next_level_exp": 100,
        "level": 5,
        "moves": []
    }
    run_async(fake_db.pokemons.insert_one(pokemon_doc))

    trainer_doc = {
        "suid": 1,
        "username": "user1",
        "name": "Angel",
        "wins": 0,
        "losses": 0,
        "team": ["1"],
        "inventory": ["1"]
    }
    run_async(fake_db.trainers.insert_one(trainer_doc))

    team = test_client.get("/api/trainers/1/team", headers=headers)
    assert team.status_code == 200
    assert team.json()["team_size"] == 1

    win = test_client.post("/api/trainers/1/stats/win", headers=headers)
    assert win.status_code == 200
    assert win.json()["wins"] == 1


def test_update_experience_and_evolution(client):
    test_client, fake_db = client
    headers = _auth_headers(test_client)

    base_pokemon = {
        "pokedex_id": "1",
        "name": "bulbasaur",
        "type": "GRASS",
        "experience": 0,
        "next_level_exp": 100,
        "level": 5,
        "evolution": {"next_id": "2", "level": 6},
        "moves": []
    }
    evolved_pokemon = {
        "pokedex_id": "2",
        "name": "ivysaur",
        "type": "GRASS",
        "experience": 0,
        "next_level_exp": 100,
        "level": 5,
        "moves": []
    }
    run_async(fake_db.pokemons.insert_one(base_pokemon))
    run_async(fake_db.pokemons.insert_one(evolved_pokemon))

    trainer_doc = {
        "suid": 2,
        "username": "user1",
        "name": "Angel",
        "wins": 0,
        "losses": 0,
        "team": ["1"],
        "inventory": ["1"]
    }
    run_async(fake_db.trainers.insert_one(trainer_doc))

    response = test_client.put(
        "/api/trainers/2/pokemons/0",
        headers=headers,
        json={"experience": 120}
    )
    assert response.status_code == 200
    body = response.json()
    assert body["evolved"] is True
    assert body["pokedex_id"] == "2"
    assert body["level"] == 6
    assert body["experience"] == 20

    updated_trainer = run_async(fake_db.trainers.find_one({"suid": 2}))
    assert updated_trainer["team"][0] == "2"
    assert updated_trainer["inventory"][0] == "2"
