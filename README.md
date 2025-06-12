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
Para el historial de los movimienros realizados en la batalla se ha usado una pila Stack<String> la cual se usa para guardar los eventos que ocurren durante la batalla Pokémon, como ataques, cambios de Pokémon y el inicio o fin de la batalla. Cada vez que ocurre un evento, se guarda una línea con la descripción en la parte superior de la pila. Esta estructura sigue el orden LIFO (último en entrar, primero en salir), lo que permite acceder fácilmente al último evento registrado.
<br>
Usar una pila es útil porque mantiene el orden natural de los eventos, facilita funciones como mostrar o deshacer el último movimiento, es eficiente en rendimiento, y ayuda a mantener la lógica organizada sin permitir inserciones en posiciones aleatorias, como pasaría con otras estructuras como listas o colas.
