FROM node:10.8-alpine

RUN mkdir -p /usr/app
COPY . /usr/app/

WORKDIR /usr/app

ENTRYPOINT [ "npm", "start" ]
EXPOSE 3000