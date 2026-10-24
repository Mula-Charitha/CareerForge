import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // Expose VITE_* env vars to the client bundle
  envPrefix: 'VITE_',
  build: {
    outDir: 'dist',
    sourcemap: false,
  },
})
