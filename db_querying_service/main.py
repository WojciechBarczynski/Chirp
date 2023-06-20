import tomli

from flask import Flask, request
from flask_cors import CORS

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



@app.route('/execute', methods=['POST'])
def execute():
    if request.method != 'POST':
        raise "Improper request method"
    query = request.args.get('query')[1:-1]
    print(f"query: {query}")
    records, _summary, _keys = db_driver.execute_query(query)

    records_data = []
    for record in records:
        node = record.items()[0][1]
        record_properties = node._properties
        record_properties["id"] = parse_id(node._element_id)
        records_data.append(record_properties)

    return records_data

def parse_id(element_id):
    id = ""
    for l in element_id[::-1]:
        if l == ":":
            return id
        id = l + id
    else:
        raise "Parsing id failed"

if __name__ == '__main__':
    app.run(debug=True)
