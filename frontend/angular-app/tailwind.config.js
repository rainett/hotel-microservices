/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      fontFamily: {
        'cinzel': ['Cinzel Decorative', 'serif']
      },
      backgroundImage: {
        'hero': "url('./assets/hero-image-compressed.jpg')",
      }
    },
  },
  plugins: [],
}
