#!/bin/bash

# Simple script to start ChromaDB server for development
# This creates a standalone ChromaDB server instance

echo "Starting ChromaDB server on localhost:8000..."

python3 -c "
import chromadb
from chromadb.config import Settings
import time

# Create ChromaDB HTTP server
server = chromadb.HttpClient(
    host='0.0.0.0',
    port=8000,
    settings=Settings(
        chroma_server_host='0.0.0.0',
        chroma_server_http_port=8000,
        is_persistent=True,
        persist_directory='./chroma-data'
    )
)

print('ChromaDB server started on http://localhost:8000')
print('Data will be stored in ./chroma-data directory')
print('Press Ctrl+C to stop the server')

try:
    while True:
        time.sleep(1)
except KeyboardInterrupt:
    print('\nStopping ChromaDB server...')
"