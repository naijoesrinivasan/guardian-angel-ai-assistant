from email.message import EmailMessage
import smtplib
import json

PASSWORD = 'YOUR PASSWORD'
USER = 'YOUR EMAIL'

def get_emails():
    file = open('emails.json')
    data = json.load(file)
    for i in data:
        print(i)
    file.close()
    return data

def send_email(subject, body, to):
    email_dictionary = get_emails()
    to_name = to.split('@')[0]
    to = email_dictionary[to_name.lower()]
    print("To:", to)
    print("subject:", subject)
    print("body", body)
    try:
        msg = EmailMessage()
        msg.set_content(body)
        msg['subject'] = subject
        msg['to'] = to
        msg['from'] = USER
        server = smtplib.SMTP('smtp.gmail.com', 587)
        server.starttls()
        server.login(USER, PASSWORD)
        server.send_message(msg)
        server.quit()
        return "Email Sent!"
    except:
        return "A problem occured while sending email."

#send_email(to='joshiamogh9@gmail.com', subject="Are you ready for tomorrow's birthday party?", body="Hi, pls dont come.")
