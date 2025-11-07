<?php
include 'conexion.php';
// Consulta directa y obtención de resultados en un bucle reducido
$res = mysqli_query($conexion, "SELECT * FROM usuarios");
$datos = [];
//el while saca los elementos de la consulta res y lo guarda en f, después los datos de f se añaden al array datos
while ($f = mysqli_fetch_assoc($res)) $datos[] = $f;

// Salida JSON directa
echo json_encode($datos);
?>