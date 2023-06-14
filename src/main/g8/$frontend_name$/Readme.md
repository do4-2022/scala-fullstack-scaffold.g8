# Scalajs frontend app

## Backend url env variable

VITE_API_URL points to the api of the project this variable is read during the build phase.

If the frontend is served by the backend, VITE_API_URL needs to be an empty string `""`.

## Dependencies installation

Install the JS dependencies using npm :

```
npm i
```

## Dev env

To start the frontend in dev you need to execute two commands simultaneously :

- vite bundler
  
  ```
  npm run dev
  ```
- sbt compilation
  ```
  sbt
  [...]
  sbt:$frontend_name;format="snake"$> ~fastLinkJS
  ```

## Production build

To get a production build, run :

```
npm run build
```

All the frontend content and code is then available in the `dist` folder. This folder needs to be served as the root of the website (so that `index.html` is at the root `/`)

This can be done with :

```
npm run preview
```
