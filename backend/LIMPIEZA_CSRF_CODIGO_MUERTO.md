# Revisión CSRF y limpieza de código

## Seguridad CSRF

Se ha dejado CSRF activo para la parte web y se ha excluido únicamente la API REST:

```java
.csrf(csrf -> csrf
        .ignoringRequestMatchers("/api/**")
)
```

Esto permite que:
- los formularios web queden protegidos con token CSRF;
- la aplicación móvil pueda seguir consumiendo los endpoints `/api/**` sin token CSRF.

## Formularios web

Se ha añadido el token CSRF explícito en los formularios `POST` de Thymeleaf:

```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
```

## Limpieza realizada

- Eliminados comentarios generados por NetBeans que no aportaban documentación real.
- Eliminados imports sin uso detectados en clases Java.
- Sustituidos logs generados reales por `.gitkeep` y `README.md` en la carpeta `logs`.
- Añadidas reglas `.gitignore` para no versionar ficheros `.log`, `.gz` ni temporales.
- Mantenida la configuración de logs a fichero y la documentación existente.

## Nota

No se han eliminado clases, controllers, services ni repositories porque muchos son usados por Spring mediante inyección/anotaciones aunque no aparezcan como referencias directas en el código.
