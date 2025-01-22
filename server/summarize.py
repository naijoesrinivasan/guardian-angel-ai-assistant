import openai

def text_summarize(text, type=str):
    openai.api_key="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"

    chat_completion = openai.ChatCompletion.create(
        messages=[
            {
                "role": "user",
                "content": "Summarize the following text: " + str(text),
            }
        ],
        model="gpt-3.5-turbo",
    )
    output = chat_completion.choices[0].message.content
    print("Summarized Text:\n" + output)
    return output

if __name__ == "__main__":
    text = input("Enter the text to summarize: ")
    text_summarize(text)