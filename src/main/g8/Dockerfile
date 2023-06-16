FROM node:18-alpine as build
COPY . . 
RUN npm ci
RUN npm run build

FROM node:18-alpine as run
WORKDIR /app
COPY --from=build /dist . 
RUN npm i serve
EXPOSE 3000
ENTRYPOINT ["npm", "run", "preview"]