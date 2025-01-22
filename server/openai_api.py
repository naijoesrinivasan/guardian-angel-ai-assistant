import openai
import json
import os
import requests
# load and set our key
openai.api_key = "YOUR_API_KEY"

function_descriptions = [
    {
        "name": "text_summarize",
        "description": "Summarize a given text",
        "parameters": {
            "type": "object",
            "properties": {
                "text": {
                    "type": "string",
                    "description": "The summarized text",
                }
            },
            "required": ["text"],
        },
     },
    {   # denote the schedule_task function, describe what it does and mention the parameters date, time and task. 
        "name": "schedule_task",
        "description": "Schedule a task based on given information",
        "parameters": {
            "type": "object",
            "properties": {
                "date": {
                    "type": "string",
                    "description": "The date on which the task should be scheduled",
                },
                 "start_time": {
                    "type": "string",
                    "description": "The time at which the task should start",
                },
                "end_time": {
                    "type": "string",
                    "description": "The time at which the task should end",
                },
                 "task": {
                    "type": "string",
                    "description": "The name of the task",
                }
            },
            "required": ["date", "start_time", "task"],
        },
     },
     {  # denote the recommend_song_by_name function, describe what it does and mention the parameters date, time and task. 
        "name": "recommend_song_by_name",
        "description": "Play the given song",
        "parameters": {
            "type": "object",
            "properties": {
                "song": {
                    "type": "string",
                    "description": "The song mentioned in the prompt",
                }
            },
            "required": ["song"],
        },
     },
        {
        "name": "recommend_song_by_mood",
        "description": "Song recommendation based on mood",
        "parameters": {
            "type": "object",
            "properties": {
                "mood": {
                    "type": "string",
                    "description": "The mood mentioned in the prompt",
                }
            },
            "required": ["mood"],
        },
     },
     {
        "name": "recommend_song_by_artist",
        "description": "Song recommendation based on an artist or mood",
        "parameters": {
            "type": "object",
            "properties": {
                "artist": {
                    "type": "string",
                    "description": "The artist mentioned in the prompt",
                }

            },
            "required": ["artist"],
        },
    },
     {
        "name": "send_email",
        "description": "template to have an email sent",
        "parameters": {
            "type": "object",
            "properties": {
                "recipient": {
                    "type": "string",
                    "description": "The receipient of the email",
                },
                "subject": {
                    "type": "string",
                    "description": "Reason for sending the email",
                },
                 "body": {
                    "type": "string",
                    "description": "Body of the email",
                },
            },
            "required": ["recipient", "subject", "body"],
        },
    },

]

def ask_and_reply(prompt):
    """Give LLM a given prompt and get an answer."""

    completion = openai.ChatCompletion.create(
        model="gpt-3.5-turbo-0613",
        messages=[{"role": "user", "content": prompt}],
        # add function calling
        functions=function_descriptions,
        function_call="auto",  # specify the function call
    )

    output = completion.choices[0].message
    return output

if __name__ == "__main__":
    text = input("Enter the prompt: ")
    output = ask_and_reply(text)
    print(output)
    function = output["function_call"]["name"]
    print(function)

# Uncomment the following code to test the functionality.
# Dependency - pip install openai==0.28
# prompt =  "Play a song by Iron Maiden"
# prompt =  "Feeling morose. Play something that might cheer me up"
# prompt =  "Play the song Hymn for the weekend"
# object_reply = ask_and_reply(prompt)
# print(prompt)
# print(object_reply['function_call']["name"])
# for k,v in json.loads(object_reply['function_call']["arguments"]).items():
#   print(k, v)
