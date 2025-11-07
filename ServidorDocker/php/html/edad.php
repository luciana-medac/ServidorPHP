<?php
include 'conexion.php'

//Recibimos el nombre buscado por parámetro GET
$nombre_buscado = $_GET['nombre'];

//Consulta buscando coincidencias exactas por nombre
$res = mysqli_query($conexion, "SELECT * FROM usuarios WHERE nombre = '$nombre_buscado");

//Obtenemos el primer resultado que coincida
$usuario = mysqli_fetch_assoc($res);

//Devolvemos el usuarios encontrado (o null si no esite ninguno)
echo json_encode($usuario);
?>