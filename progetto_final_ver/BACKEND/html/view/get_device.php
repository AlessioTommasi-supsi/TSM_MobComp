<?php
header("Content-Type: application/json; charset=UTF-8");

include_once '../controller/Database.php';


//$latitude = isset($_REQUEST['latitude']) ? $_REQUEST['latitude'] : null;

$database = new Database();
$db = $database->getConnection();

$query = "SELECT device, COUNT(ID) AS num_logs FROM Log GROUP BY device";

$stmt = $db->prepare($query);
$stmt->execute();

$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

header('Content-Type: application/json');
echo json_encode($result);
?>