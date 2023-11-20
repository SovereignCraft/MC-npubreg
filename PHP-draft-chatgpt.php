<?php
header('Content-Type: application/json');

// Configuration
$secretPhrase = 'your_secret_phrase_here'; // Set your secret phrase here
$jsonFilePath = 'output.json'; // Path to the JSON file you want to write to

// Check for the POST request
if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    // Check for the secret phrase
    if (isset($_POST['secret']) && $_POST['secret'] === $secretPhrase) {
        
        // Check for the data parameter and write to JSON file
        if (isset($_POST['data'])) {
            $data = $_POST['data'];

            if (file_put_contents($jsonFilePath, json_encode($data))) {
                echo json_encode(['success' => true, 'message' => 'Data written successfully!']);
            } else {
                echo json_encode(['success' => false, 'message' => 'Failed to write data to JSON file.']);
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
