<?php
header('Content-Type: application/json');

// Configuration
$secretPhrase = 'your_secret_phrase_here'; // Set your secret phrase here
$jsonFilePath = 'output.json'; // Path to the JSON file you want to write to

// Check for the POST request
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Get raw POST data
    $rawData = file_get_contents("php://input");
    $decodedData = json_decode($rawData, true);

    // Check for the secret phrase
    if (isset($decodedData['secret']) && $decodedData['secret'] === $secretPhrase) {

        // Check for the data parameter and write to JSON file
        if (isset($decodedData['data'])) {
            $data = $decodedData['data'];

            // Validate data as JSON
            if (json_last_error() === JSON_ERROR_NONE) {
                if (file_put_contents($jsonFilePath, json_encode($data))) {
                    echo json_encode(['success' => true, 'message' => 'Data written successfully!']);
                } else {
                    echo json_encode(['success' => false, 'message' => 'Failed to write data to JSON file.']);
                }
            } else {
                echo json_encode(['success' => false, 'message' => 'Invalid JSON data.']);
            }
        } else {
            echo json_encode(['success' => false, 'message' => 'Data parameter missing.']);
        }
    } else {
        echo json_encode(['success' => false, 'message' => 'Invalid secret phrase.']);
    }
} else {
    echo json_encode(['success' => false, 'message' => 'Invalid request method.']);
}
?>
