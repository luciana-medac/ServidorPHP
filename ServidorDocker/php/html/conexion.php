<?php
//establece la conexión, pasandole la imagen, usuario, contraseña 
$conexion = mysqli_connect("db","admin","admin1234","bd_android")
    or die("Error de conexión con la base de datos");
//esto establece lenguaje UTF-8
mysqli_set_charset($conexion, "utf8mb4");
?>