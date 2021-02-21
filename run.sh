export ENABLE_SERVER_SESSION=${ENABLE_SERVER_SESSION:=false}
export FIREBASE_DATABASE=${FIREBASE_DATABASE:="https://vocon-intellij-frontend.firebaseio.com"}
export CORS_DOMAIN=${CORS_DOMAIN:=localhost}
export GOOGLE_APPLICATION_CREDENTIALS=${GOOGLE_APPLICATION_CREDENTIALS:="src/main/resources/firebase-server-config.json"}
export SUPER_ADMINS=oliver.veits@vocon-it.com

[ ! -r "${GOOGLE_APPLICATION_CREDENTIALS}" ] \
  && echo "Please copy the firebase server config to ${GOOGLE_APPLICATION_CREDENTIALS}" >&2 \
  && exit 1

echo "Running server. Press Ctrl-C for stopping it again..."
mvn spring-boot:run

# run in the background (not used because mvn spring-boot:stop fails):
#mvn spring-boot:start \
#  && echo "Server started. To stop it again, type 'mvn spring-boot:stop'"

# For running the application and tests in IntelliJ, use
# SUPER_ADMINS=oliver.veits@vocon-it.com;ENABLE_SERVER_SESSION=false;GOOGLE_APPLICATION_CREDENTIALS=src/main/resources/firebase-server-config.json;FIREBASE_DATABASE=https://vocon-intellij-frontend.firebaseio.com;CORS_DOMAIN=localhost
# in both:
# 1. IntelliJ -> Run -> Edit configurations... -> Application --> ApiServiceApplicaton -> Environment variables
# 2. IntelliJ -> Run -> Edit configurations... -> JUnit --> ApiServiceApplicatonTests -> Environment variables
