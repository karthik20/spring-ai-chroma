#!/bin/bash

# Docker entrypoint script to start both ChromaDB and Spring Boot application

set -e

echo "Starting ChromaDB server..."

# Start ChromaDB server in the background
python3 -c "
import chromadb
from chromadb.config import Settings
import time
import threading

def start_chroma_server():
    try:
        # Create ChromaDB server with persistent storage
        client = chromadb.HttpClient(
            host='0.0.0.0',
            port=8000,
            settings=Settings(
                chroma_server_host='0.0.0.0',
                chroma_server_http_port=8000,
                is_persistent=True,
                persist_directory='/chroma/data'
            )
        )
        print('ChromaDB server started on port 8000')
        
        # Keep the server running
        while True:
            time.sleep(1)
    except Exception as e:
        print(f'Failed to start ChromaDB server: {e}')

# Start ChromaDB in a separate thread
chroma_thread = threading.Thread(target=start_chroma_server, daemon=True)
chroma_thread.start()

# Wait a bit for ChromaDB to start
time.sleep(5)
print('ChromaDB server should be running...')
" &

# Wait for ChromaDB to be ready
echo "Waiting for ChromaDB to be ready..."
sleep 10

# Start the Spring Boot application
echo "Starting Spring Boot application..."
exec java -jar /app/spring-ai-chroma.jar "$@"