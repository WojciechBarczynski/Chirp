import tomli

from flask import Flask, request, jsonify
from flask_cors import CORS, cross_origin

from neo4j import GraphDatabase


app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'

with open("db_config.toml", "rb") as file:
    db_config = tomli.load(file)

neo4j_url = db_config["NEO4J_URI"]
neo4j_username = db_config["NEO4J_USERNAME"]
neo4j_password = db_config["NEO4J_PASSWORD"]

db_driver = GraphDatabase.driver(
    neo4j_url, auth=(neo4j_username, neo4j_password))


@app.route('/write', methods=['POST'])
def write():
    if request.method != 'POST':
        raise "Improper request method"
    query = request.args.get('query')[1:-1]
    _records, _summary, _keys = db_driver.execute_query(query)
    return ""


@app.route('/read', methods=['GET'])
def read():
    if request.method != 'GET':
        raise "Improper request method"
    query = request.args.get('query')[1:-1]
    records, _summary, _keys = db_driver.execute_query(query)
    records = [list(record.data().values()) for record in records]
    flat_list = [num for sublist in records for num in sublist]
    return flat_list

if __name__ == '__main__':
    app.run(debug=True)
