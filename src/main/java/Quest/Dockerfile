FROM python:alpine3.19


WORKDIR /app

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 80

ENV flag "HackMaze{FLAG_PLACEHOLDER}"

CMD ["python", "app.py", "HackMaze{FLAG_PLACEHOLDER}"]
