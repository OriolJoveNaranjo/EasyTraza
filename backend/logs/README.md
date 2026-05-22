# Logs EasyTraza

Aquesta carpeta és el destí per defecte dels fitxers de log de l'aplicació.

Fitxers generats en executar el backend:

- `easytraza.log`: registre general de l'aplicació.
- `easytraza-warn-error.log`: avisos i errors útils per a administració i manteniment.
- `easytraza-error.log`: errors i excepcions crítiques.

Els fitxers es roten automàticament per data i mida segons `src/main/resources/logback-spring.xml`.
