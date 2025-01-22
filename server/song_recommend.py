from flask import Flask, request, make_response, render_template
from dotenv import load_dotenv
import base64
import webbrowser
import os
from requests import post, get, put
import json


load_dotenv()

client_id = os.getenv("CLIENT_ID")
client_secret = os.getenv("CLIENT_SECRET")

#get authentication token
def get_token():
    auth_string = client_id + ":" + client_secret
    auth_bytes = auth_string.encode("utf-8")
    auth_base64 = str(base64.b64encode(auth_bytes), "utf-8")

    url = "https://accounts.spotify.com/api/token"
    headers = {
        "Authorization": "Basic " + auth_base64,
        "Content-Type": "application/x-www-form-urlencoded"
    }
    data = {"grant_type": "client_credentials"}
    result = post(url, headers=headers, data=data)
    json_result = json.loads(result.content)
    token = json_result["access_token"]

    return token

#authorizations headers for API calls
def get_auth_header(token):
    return {"Authorization": "Bearer " + token}

#search for song based on song name
#@app.route('/search/song')
def search_for_song(token, song_name):
    url = "https://api.spotify.com/v1/search"
    headers = get_auth_header(token)
    query = f"?q={song_name}&type=track&limit=1"

    query_url = url + query

    result = get(query_url, headers=headers)
    json_result = json.loads(result.content)["tracks"]["items"][0]["external_urls"]["spotify"]

    if len(json_result) == 0:
        print("No song with this name exists...")
        return None

    return json_result

#search for song based on artist name
###@app.route('/search/artist')
def search_for_artist(token, artist_name):
    url = "https://api.spotify.com/v1/search"
    headers = get_auth_header(token)
    query = f"?q={artist_name}&type=artist&limit=1"

    query_url = url + query

    result = get(query_url, headers=headers)
    json_result = json.loads(result.content)["artists"]["items"][0]["external_urls"]["spotify"]

    if len(json_result) == 0:
        print("No artist with this name exists...")
        return None

    return json_result

def get_songs_by_artist(token, artist_id):
    url = f"https://api.spotify.com/v1/artists/{artist_id}/top-tracks?country=US"
    headers = get_auth_header(token)

    result = get(url, headers=headers)
    json_result = json.loads(result.content)["tracks"]


    return json_result

#search for song based on mood
#@app.route('/search/mood')
def search_by_mood(token, mood):
    url = "https://api.spotify.com/v1/search" 
    headers = get_auth_header(token)
    query = f"?q={mood}&type=playlist&limit=1"

    query_url = url + query

    result = get(query_url, headers=headers)
    json_result = json.loads(result.content)["playlists"]["items"][0]["external_urls"]["spotify"]

    if len(json_result) == 0:
        print("Unable to recommend a song...")
        return None

    return json_result
