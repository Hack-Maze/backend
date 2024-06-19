#!/usr/bin/env python3
# Author: juba0x00 | HackMaze
# Version: 1.0
 
import base64
from json import loads
from os import environ, path
from secrets import token_hex

from flask import Flask, request, jsonify
from flask_jwt_extended import JWTManager, create_access_token, jwt_required
from werkzeug.security import generate_password_hash, check_password_hash

flag = environ.get('flag')

app = Flask(__name__)
blacklist = ["proc", "self", "home", "etc"]

# Configure JWT
app.config['JWT_SECRET_KEY'] = token_hex(16)  # This should be kept secret in production
jwt = JWTManager(app)

# Define roles
roles = {
    'admin': ['readfile'],
    'user': [] 
}

users = {}
default_admin_password = token_hex(16)
users['admin'] = {'password': generate_password_hash(default_admin_password), 'role': 'admin'}
# Endpoint to register a new user
@app.route('/', methods=['GET'])
def index():
    return "Welcome, traveler, to the realm of secrets. Seek and you shall find."


@app.route('/signup', methods=['POST'])
def signup():
    username = request.json.get('username', None)
    password = request.json.get('password', None)
    
    if username in users:
        return jsonify({"msg": "Username already exists"}),  400

    hashed_password = generate_password_hash(password)
    users[username] = {'password': hashed_password, 'role': 'user'}
    return jsonify({"msg": "User registered successfully"}),  201

# Function to check permissions
def has_permission(user, permission):
    user_data = users.get(user)
    if user_data:

        user_role = user_data['role']
        return permission in roles[user_role]
    return False

# Endpoint to generate tokens
@app.route('/login', methods=['POST'])
def login():
    username = request.json.get('username', None)
    password = request.json.get('password', None)

    if username not in users:
        return jsonify({"msg": "The gates to this realm remain closed to the uninitiated, signup first"}),  404

    if not check_password_hash(users[username]['password'], password):
        return jsonify({"msg": "Your attempts to breach our defenses are in vain."}),  401

    access_token = create_access_token(identity=username)
    return jsonify(access_token=access_token),  200

@app.route('/read')
def readfile():
    auth_header = request.headers.get('Authorization')
    if auth_header:
        token = auth_header.split()[1]
        parts = token.split('.')
        padding = '=' * ((4 - len(parts[1]) %  4) %  4)
        current_user = loads(base64.b64decode(parts[1] + padding).decode('utf-8'))['sub']
        
        if current_user:
            if not has_permission(current_user, 'readfile'):
                return jsonify({"msg": "You lack the necessary quirk to access this forbidden knowledge."}),   403
            if request.args.get('file'):
                file = request.args.get('file').encode('utf-8')
                if any([blacklisted in file.decode('utf-8') for blacklisted in blacklist]):
                    return "BLOCKED: You're a villain",   401
                try:
                    content = open(path.join("uploads/", file.decode('utf-8'))).read()
                    return content
                except Exception as e:
                    return "An error occurred"
            else:
                return "Please enter ?file"
        else:
            return jsonify({"msg": "Invalid token"}),   401
    else:
        return "You're quirkless",   401


@app.route('/upload', methods=['POST'])
@jwt_required()
def upload():
    if 'file' not in request.files:
        return 'No file part'

    file = request.files['file']

    if file.filename == '':
        return 'No selected file'

    content = file.read().decode('utf-8')
    with open(path.join("uploads/", file.filename), 'w') as f:
        f.write(content)
    return 'File uploaded'

if __name__ == '__main__':
    if not path.exists("uploads"):
        from os import mkdir
        mkdir("uploads")

    app.run(host='0.0.0.0', port=80, debug=False)
