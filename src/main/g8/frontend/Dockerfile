FROM node:18-alpine as build

# TODO: change the API URL 
ENV VITE_API_URL http://localhost:8080

WORKDIR /app
COPY . . 
RUN npm ci
RUN npm run build

FROM nginx:1.25
WORKDIR /app
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 3000