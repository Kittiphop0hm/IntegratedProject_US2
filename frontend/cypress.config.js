import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    specPattern: "cypress/e2e/**/*.{cy,spec}.{js,jsx,ts,tsx}",
    baseUrl: "http://localhost:5173/",
    baseAPI: "http://localhost:8080/itb-mshop/",
    experimentalRunAllSpecs: true,
    // baseUrl: "http://intproj24.sit.kmutt.ac.th/us2",
    // baseAPI: "http://intproj24.sit.kmutt.ac.th:8080/us2/itb-mshop",
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});
