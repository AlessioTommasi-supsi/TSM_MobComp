<?php
header("Content-Type: application/json; charset=UTF-8");

include_once '../controller/Database.php';


$device = isset($_REQUEST['device']) ? $_REQUEST['device'] : null;

$database = new Database();
$db = $database->getConnection();



if ($device !== null) {
    $query = "SELECT * FROM Log WHERE device = :device ORDER BY date DESC";
    $stmt = $db->prepare($query);
    $stmt->bindParam(':device', $device);
}else{
    $query = "SELECT * FROM Log ORDER BY device ASC, date DESC";
    $stmt = $db->prepare($query);
}

$stmt->execute();

$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

header('Content-Type: application/json');
echo json_encode($result);
?>