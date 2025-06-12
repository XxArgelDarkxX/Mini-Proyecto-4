# Mini Proyecto #4 / Batalla Pokemon
# Integrantes
Angel Nicolas Castañeda Valencia - 202459426
<br>
Camilo Andres Riscanevo Cotrina - 202459753
<br>
Karen Andrea Sanabria Gonzalez - 202459413
<br>
<br> 
# Estructuras
<br>
<br>
PILA
<br>
<br>
Para el historial de los movimienros realizados en la batalla se ha usado una pila Stack<String> la cual se usa para guardar los eventos que ocurren durante la batalla Pokémon, como ataques, cambios de Pokémon y el inicio o fin de la batalla. Cada vez que ocurre un evento, se guarda una línea con la descripción en la parte superior de la pila. Esta estructura sigue el orden LIFO (último en entrar, primero en salir), lo que permite acceder fácilmente al último evento registrado.
<br>
Usar una pila es útil porque mantiene el orden natural de los eventos, facilita funciones como mostrar o deshacer el último movimiento, es eficiente en rendimiento, y ayuda a mantener la lógica organizada sin permitir inserciones en posiciones aleatorias, como pasaría con otras estructuras como listas o colas.
<br>
<br>
HASHMAP
<br>
<br>
En este proyecto, se utiliza la estructura HashMap para almacenar y buscar Pokémon de forma rápida. Un HashMap es una colección que permite guardar pares clave-valor, donde se puede acceder a los valores usando su clave de forma eficiente.
<br>
Por ejemplo, se puede usar un HashMap para guardar Pokémon por su nombre. En este caso, la clave es el nombre del Pokémon (como "Pikachu") y el valor es el objeto Pokémon completo. Esto permite encontrar un Pokémon directamente por su nombre sin tener que recorrer una lista.
