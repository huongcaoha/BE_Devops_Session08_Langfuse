import urllib.request
import json

url = 'https://generativelanguage.googleapis.com/v1beta/openai/embeddings'
headers = {
    'Authorization': 'Bearer AIzaSyDjM0W2FizqoWDJt5za4Dx2aWdHR14ntS0',
    'Content-Type': 'application/json'
}

models_to_test = ['text-embedding-004', 'gemini-embedding-001', 'embedding-001', 'text-embedding-004']

for model in models_to_test:
    data = {
        'input': 'hello world',
        'model': model
    }
    
    req = urllib.request.Request(url, data=json.dumps(data).encode('utf-8'), headers=headers)
    try:
        response = urllib.request.urlopen(req)
        print(f"SUCCESS: {model}")
        resp_data = json.loads(response.read().decode('utf-8'))
        if 'data' in resp_data and len(resp_data['data']) > 0:
            print(f"Dimension: {len(resp_data['data'][0]['embedding'])}")
        else:
            print("No embedding in response")
    except urllib.error.HTTPError as e:
        print(f"FAILED: {model}")
        print(e.read().decode('utf-8'))
