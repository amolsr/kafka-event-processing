# Fetching the minified node image on apline linux
FROM node:14.21.3-alpine

RUN apk update && apk add bash

# Declaring env
ENV NODE_ENV development

# Setting up the work directory
WORKDIR /express-docker

# Copying all the files in our project
COPY ./express-app .

RUN apk add --no-cache --virtual .gyp python3 make g++ libc6-compat bash\
    && npm install \
    && apk del .gyp

# Starting our application
CMD [ "node", "app.js" ]

# Exposing server port
EXPOSE 3000