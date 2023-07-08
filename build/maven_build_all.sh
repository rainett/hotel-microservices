#!/bin/bash

echo "--------------------"
projects_dir=".."
projects=("config-microservice" "discovery-microservice" "gateway-microservice" "reservation-microservice" "room-microservice")

for project in "${projects[@]}"
do
  project_dir="$projects_dir/$project"
  echo "Building project: $project_dir"

  cd "$project_dir" || { echo "Failed to change directory to $project_dir"; exit 1; }

  mvn clean install || { echo "Failed to build $project_dir"; exit 1; }

  echo "Build successful for $project_dir"
done
