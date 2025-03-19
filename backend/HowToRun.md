## Как запустить
### 1. Скачать Docker и docker-compose
    sudo apt install docker.io
    sudo apt install docker-compose

### 2. выполнить в директории `bip-project/`
#### При первом запуске с опцией `--build`
    sudo docker-compose up --build -d
#### Последующие запуски:
    sudo docker-compose up -d