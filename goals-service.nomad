job "goals-job" {
  datacenters = ["home"]
  type        = "service"

  group "goals-group" {
    count = 1

    #constraint {
    #  operator = "distinct_hosts"
    #  value = "true"
    #}

    constraint {
      attribute = "${node.class}"
      value = "guestworker"
    }

    restart {
      attempts = 10
      interval = "5m"
      delay    = "25s"
      mode     = "delay"
    }

    network {
      port "web" {
        static = 8080
      }
    }

    task "goals-task" {
      driver = "docker"

      template {
        data = <<EOH
BOT_TOKEN="{{ key "telegram/bot/accounter/token" }}"
POSTGRES_URL="{{ key "postgres.jdbc.url" }}"
POSTGRES_DRIVER="{{ key "postgres.jdbc.driver" }}"
POSTGRES_USER="{{ key "postgres.jdbc.user" }}"
POSTGRES_PASSWORD="{{ key "postgres.jdbc.password" }}"
OBJECT_STORAGE_ENDPOINT="{{ key "expenses/object/storage/fs.s3a.endpoint" }}"
OBJECT_STORAGE_KEY="{{ key "expenses/object/storage/fs.s3a.access.key" }}"
OBJECT_STORAGE_SECRET="{{ key "expenses/object/storage/fs.s3a.secret.key" }}"
GOALS_BASE_URL="{{ key "telegram/bot/accounter/goals.base.url" }}"
EOH
        destination = "secrets.env"
        env = true
      }

      config {
        image = "127.0.0.1:9999/docker/goals-service:0.0.3"
        
        ports = ["web"]
      }

      resources {
        cpu    = 500
        memory = 512
      }

      service {
        name = "goals-service"
        port = "web"
        tags = ["urlprefix-/goals strip=/goals", "urlprefix-/"]

        check {
          type     = "http"
          path     = "/actuator/health"
          interval = "10s"
          timeout  = "10s"
        }
      }
    }
  }
}
