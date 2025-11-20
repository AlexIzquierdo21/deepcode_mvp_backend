# Comandos Docker - DeepCode Backend

## Construir y ejecutar (primera vez)
bash
# Construir imágenes y levantar contenedores
docker-compose up --build -d

# Ver logs en tiempo real
docker-compose logs -f app

# Ver estado de contenedores
docker-compose ps


## ▶Iniciar/Detener (después de la primera vez)
bash
# Iniciar contenedores
docker-compose up -d

# Detener contenedores (conserva datos)
docker-compose down

# Detener y eliminar TODO (incluye volumen BD)
docker-compose down -v


## Debugging
bash
# Ver logs de la app
docker-compose logs -f app

# Ver logs de MySQL
docker-compose logs -f mysql

# Entrar al contenedor de la app (bash)
docker exec -it deepcode-app sh

# Entrar a MySQL
docker exec -it deepcode-mysql mysql -uroot -proot deepcode_db


## Limpiar todo
bash
# Detener y eliminar contenedores + volúmenes
docker-compose down -v

# Limpiar imágenes no usadas
docker system prune -a
```

## Reconstruir solo la app (tras cambios en código)
bash
# Reconstruir imagen de la app
docker-compose build app

# Reiniciar solo el contenedor app
docker-compose up -d app


## URLs de acceso

- **API Backend:** http://localhost:8080
- **MySQL:** localhost:3307 (usuario: root, password: root)
- **Health Check:** http://localhost:8080/actuator/health (si tienes actuator)