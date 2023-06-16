const withNextra = require('nextra')({
  theme: 'nextra-theme-docs',
  themeConfig: './theme.config.tsx',
  defaultShowCopyCode: true,
})
const isInProduction = process.env.ENVIRONMENT === 'production'

module.exports = {
  ...withNextra(),
  images: {
    unoptimized: true,
  },
  basePath: isInProduction ? "/scala-fullstack-scaffold.g8" : undefined
};
