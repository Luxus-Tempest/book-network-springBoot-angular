FROM node:20.10.0 AS build-stage

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=build-stage /app/dist/book-network-front /usr/share/nginx/html

EXPOSE 80

# Command pour creer l'image docker du frontend : bsn/bsn-front:0.0.1-SNAPSHOT ----> Dans le terminal manuel
# docker build -t bsn/bsn-front:0.0.1-SNAPSHOT -f ../docker/frontend/Dockerfile .