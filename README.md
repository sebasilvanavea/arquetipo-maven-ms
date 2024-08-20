# Proyecto
[Documentar aquí la descripción del proyecto que hace uso del arquetipo.]

## Ownership

| Rol               |                                                         |
|---                |---                                                      |
| Arquitecto        |  [Indique aquí el nombre del arquitecto responsable.]   |
| Líder técnico     |  [Indique aquí el nombre del líder técnico.]            |
| Product Owner     |  [Indique aquí el nombre del product owner, si aplica.] |

# Dependencias y requerimientos

[Indique aquí las dependencias y requerimientos existentes para la ejecución del proyecto.]

# Modo de uso

[Indique aquí de forma detallada el modo de uso del presente desarrollo. Considere aspectos de instalación y ejecución/consumo. Incorpore imagenes de ser necesario.]

# Configuración de Secretos con Catálogo

Este artefacto permite la configuración de los recursos que utilizara a través de archivos .properties y para la configuración de secretos se deben configurar en el archivo "secrets.properties" que se encuentra en el path "src/main/resources".

- Dentro del archivo se pueden configurar los secretos que configuran variables de entorno a través de una lista sobre la key "secrets-env-ids", por ejemplo para obtener los valores de dos bases de datos se configura la lista de la siguiente forma:
```sh
secrets-env-ids=bbdd-ellu,redis
```
Para mas detalles sobre los valores de la lista, dirigirse a: [Catalogo Secretos Enviroments - GIT](http://bitbucket.bch.bancodechile.cl:7990/projects/DSO/repos/catalogo-vault-os/browse/vault/catalogo_vault_mipago.md?at=refs%2Fheads%2Fdev).
- Dentro del archivo se pueden configurar los secretos que configuran volúmenes a través de una lista sobre la key "secrets-volumen-ids", por ejemplo para obtener los valores de dos volúmenes para acceder a dos bases de datos se configura la lista de la siguiente forma:
```sh
secrets-volumen-ids=wallet-bbdd01,wallet-02
```
Para mas detalles sobre los valores de la lista, dirigirse a: [Catalogo Secretos Volúmenes - GIT](http://bitbucket.bch.bancodechile.cl:7990/projects/DSO/repos/catalogo-vault-os/browse/objectstorage/catalago_os_mipago.md?at=refs%2Fheads%2Fdev).