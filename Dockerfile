version: '3.8'
services:
  postgresql:
    image: postgres
    container_name: tg-postgresql
    ports:
      - '5432:5432'
    environment:
      - 'POSTGRES_ROOT_PASSWORD=postgres'
      - 'POSTGRES_USER=postgres'
      - 'POSTGRES_PASSWORD=postgres'
      - 'POSTGRES_DB=bot'
    volumes:
      - postgresql-data:/var/lib/postgresql/data
      - ./resources/:/docker-entrypoint-initdb.d/

  admin-dashboard:
    image: tg-admin-dashboard
    container_name: tg-admin-dashboard
    ports:
      - '8080:8080'
    environment:
      - 'DATABASE_URL=jdbc:postgresql://postgresql:5432/bot'
      - 'DATABASE_USERNAME=postgres'
      - 'DATABASE_PASSWORD=postgres'
    depends_on:
      - postgresql

  telegram-bot:
    image: tg-telegram-bot
    container_name: tg-telegram-bot
    environment:
      - 'DATABASE_URL=jdbc:postgresql://postgresql:5432/bot'
      - 'DATABASE_USERNAME=postgres'
      - 'DATABASE_PASSWORD=postgres'
    depends_on:
      - postgresql

volumes:
  postgresql-data:
