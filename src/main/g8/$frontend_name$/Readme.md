# Scalajs frontend app

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
