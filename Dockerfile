# ---- Stage 1: build the app ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace
COPY . .
RUN chmod +x gradlew && ./gradlew installDist --no-daemon

# ---- Stage 2: run the app ----
FROM eclipse-temurin:25-jre
RUN useradd --system --uid 10001 appuser
WORKDIR /opt/app
COPY --from=build /workspace/app/build/install/app/ ./
USER appuser
ENV PORT=7070
EXPOSE 7070
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s \
  CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/7070 && printf "GET /health HTTP/1.0\r\n\r\n" >&3 && grep -q UP <&3'
CMD ["./bin/app"]