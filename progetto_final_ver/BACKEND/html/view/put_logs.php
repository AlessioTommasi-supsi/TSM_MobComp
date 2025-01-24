<?php
header("Content-Type: application/json; charset=UTF-8");

include_once '../controller/Database.php';

$database = new Database();
$db = $database->getConnection();

$device = isset($_REQUEST['device']) ? $_REQUEST['device'] : null;
$state = isset($_REQUEST['state']) ? $_REQUEST['state'] : null;
$date = isset($_REQUEST['date']) ? $_REQUEST['date'] : null;

if($date == null){
    $date = date("Y-m-d H:i:s"); // Data attuale
}

if($device == null || $state == null ){
    http_response_code(400);
    echo json_encode(array("error" => "Missing parameters"));
    exit();
}

$query = "INSERT INTO Log (device, state, date) VALUES (:device, :state, :date)";
$stmt = $db->prepare($query);
$stmt->bindParam(':device', $device);
$stmt->bindParam(':state', $state);
$stmt->bindParam(':date', $date);

if($stmt->execute()){
    echo json_encode(array("message" => "Log entry created successfully."));
} else {
    http_response_code(500);
    echo json_encode(array("error" => "Unable to create log entry."));
}
?>
