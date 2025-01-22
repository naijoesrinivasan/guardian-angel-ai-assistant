from flask import Flask, request, make_response, render_template
import os
import json
import openai
import base64
import webbrowser
from requests import post, get, put
from datetime import datetime, timedelta
from dotenv import load_dotenv

from TaskScheduling import *
from email_utility import *
from openai_api import *
from summarize import *
from song_recommend import *


app = Flask(__name__)

@app.route("/", methods=['POST'])
def home():
    data = request.get_json()  # Get JSON data from the POST request body
    if data and "prompt" in data:
        prompt_value = data["prompt"]
    return misc(prompt_value)


@app.route('/misc', methods=['GET','POST'])
def misc(prompt):
    object_reply = ask_and_reply(prompt)
    function = object_reply["function_call"]["name"]
    
    task_output = None
    email_output = None
    text_output = None
    song_output = None
    
    if function == 'schedule_task':
        my_dict = json.loads(object_reply['function_call']["arguments"])
        task_output = get_schedule_tasks(prompt)
    
    elif function == 'send_email':
        my_dict = json.loads(object_reply['function_call']["arguments"])
        to = my_dict['recipient']
        subject = my_dict['subject']
        body = my_dict['body']
        email_output = send_email(subject, body, to)
        
    elif function == 'text_summarize':
        my_dict = json.loads(object_reply['function_call']["arguments"])
        text = my_dict['text']
        text_output = text_summarize(text)
        
    elif function == 'recommend_song_by_name':
        my_dict = json.loads(object_reply['function_call']["arguments"])
        song = my_dict['song']
        token = get_token()
        song_output = search_for_song(token, song)
        webbrowser.open(song_output)
        
    elif function == 'recommend_song_by_artist':
        my_dict = json.loads(object_reply['function_call']["arguments"])
        artist = my_dict['artist']
        token = get_token()
        song_output = search_for_artist(token, artist)
        webbrowser.open(song_output)
     
    elif function == 'recommend_song_by_mood':
        my_dict = json.loads(object_reply['function_call']["arguments"])
        mood = my_dict['mood']
        token = get_token()
        song_output = search_by_mood(token, mood)
        webbrowser.open(song_output)
        
    global_output = {}
    global_output['task']=task_output
    global_output['email']=email_output
    global_output['text']=text_output
    global_output['song']=song_output
    
    return json.dumps(global_output, indent=2)


if __name__ == "__main__":
    app.run(debug=True)
    
    
    