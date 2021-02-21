# Nextjs Demo using Client side Firebase session

## Installation

Add .env file in the project root folder with values from firebase config json and spring boot host and port

```
NEXT_PUBLIC_API_KEY=REPLACE_WITH_API_KEY
NEXT_PUBLIC_AUTH_DOMAIN=REPLACE_WITH_AUTH_DOMAIN
NEXT_PUBLIC_DB_URL=REPLACE_WITH_DB_URL
NEXT_PUBLIC_PROJECT_ID=REPLACE_WITH_PROJECT_ID
NEXT_PUBLIC_APP_ID=REPLACE_WITH_APP_ID
NEXT_PUBLIC_MIDDLEWARE_URL=REPLACE_WITH_SPRING_BOOT_URL
```
e.g. retrieve the Web API Keay from the [firebase project settings page](https://console.firebase.google.com/u/1/project/vocon-intellij-frontend/settings/general/web:M2I5MjFmZDEtNmNjZS00M2FhLWI5YTMtNjQ1M2Y5N2RkNGY3)
```shell
NEXT_PUBLIC_API_KEY=AI...
NEXT_PUBLIC_AUTH_DOMAIN=vocon-intellij-frontend.firebaseapp.com
NEXT_PUBLIC_DB_URL=https://vocon-intellij-frontend.firebaseio.com
NEXT_PUBLIC_PROJECT_ID=vocon-intellij-frontend
NEXT_PUBLIC_APP_ID=1:868209311085:web:81e3a24dc90deffeaf6e64
NEXT_PUBLIC_MIDDLEWARE_URL=localhost:8090
```

First, install the dependencies:

```bash
npm install
```

Second, run the development server:

```bash
npm run dev
# or
yarn dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.
