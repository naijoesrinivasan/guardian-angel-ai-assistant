from datetime import date
import openai
import json

openai.api_key = "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"
function_descriptions = [
    {
        "name": "schedule_task",
        "description": "Schedule a task based on given information",
        "parameters": {
            "type": "object",
            "properties": {
                "events": {
                    "type": "array",
                    "items": {
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
                            "event": {
                                "type": "string",
                                "description": "The name of the task",
                            }
                        }
                    },
                    "minItems": 1,
                    "maxItems": 50,
                    "description": "Should be an array of numbers.  The function will read in the array and sum the numbers.",
                }
            },
            "required": ["events"]
        }
    }
]


def ask_and_reply_task(prompt):
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


def get_todays_date():
    today = date.today()
    today = today.strftime("%d-%m-%Y")
    return today


def append_prompt(prompt):
    return "Assuming today date is " + get_todays_date() \
           + " ." + prompt + ". Give outputs in {Date:date,start_time:time, end_time: time,event:event} " \
                             "json format"


def get_enhanced_prompt(prompt):
    response = openai.Completion.create(
        engine="text-davinci-003",  # Use the text-davinci-003 engine
        prompt=prompt,
        max_tokens=4000,  # Adjust as needed
        temperature=0.7  # Adjust as needed
    )

    # Extract relevant information from the API response
    generated_text = response['choices'][0]['text']
    return generated_text


def get_output_json(prompt):
    object_reply = ask_and_reply_task(prompt)
    return json.loads(object_reply['function_call']["arguments"])


def get_schedule_tasks(prompt):
    prompt = append_prompt(prompt)
    enhanced_prompt = get_enhanced_prompt(prompt)
    # print(enhanced_prompt)
    events_json = get_output_json(enhanced_prompt)
    print(events_json)
    return events_json