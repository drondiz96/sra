FROM python:3.11-slim

WORKDIR /app

COPY server.py .

RUN pip install flask requests
EXPOSE 33001 
CMD ["python", "server.py"]
