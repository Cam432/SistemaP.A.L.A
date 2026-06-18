# Permisos PALA

## Objetivo

Dejar asentados los permisos funcionales de PALA para luego:

- cargarlos en el `subsistemaSeguridadBack`;
- asignarlos a roles del sistema `PALA`;
- usarlos en autorizacion de endpoints y flujos.

## Convencion

- Los roles propios de PALA son fijos:
  - `POSTULANTE`
  - `RECLUTADOR`
  - `ADMINISTRADOR`
- Los permisos se definen como claves estables en mayuscula con `_`.
- Los nombres visibles del subsistema pueden seguir siendo dinamicos, pero la clave funcional de PALA deberia mantenerse estable.

## Permisos propuestos

### Avisos

- `VER_AVISOS`
  - Permite listar avisos visibles para postulantes.
- `VER_DETALLE_AVISO`
  - Permite consultar el detalle de un aviso.
- `CONSULTAR_SOPORTE_AVISO`
  - Permite consultar empresas activas, carreras activas, tipos y subtipos para armar avisos.
- `CREAR_AVISO`
  - Permite crear avisos.
- `EDITAR_AVISO`
  - Permite actualizar datos de un aviso propio.
- `PUBLICAR_AVISO`
  - Permite publicar un aviso, pasandolo a estado `ABIERTO`.
- `PAUSAR_AVISO`
  - Permite pausar un aviso abierto.
- `REANUDAR_AVISO`
  - Permite reanudar un aviso pausado.
- `CANCELAR_AVISO`
  - Permite cancelar un aviso.
- `CONSULTAR_AVISOS_PROPIOS`
  - Permite consultar avisos del reclutador autenticado.

### Postulaciones

- `POSTULARSE_AVISO`
  - Permite generar una postulacion a un aviso.
- `SUBIR_CV_POSTULACION`
  - Permite subir o actualizar el CV asociado a la postulacion.
- `CONSULTAR_POSTULACIONES_PROPIAS`
  - Permite listar y consultar postulaciones propias del postulante.
- `CANCELAR_POSTULACION_PROPIA`
  - Permite dar de baja o cancelar una postulacion propia.
- `CONSULTAR_POSTULACIONES_RECIBIDAS`
  - Permite a reclutador consultar postulaciones de sus avisos.
- `RESOLVER_POSTULACION`
  - Permite cambiar estado de postulacion, por ejemplo `RECHAZADO`, `CITADO`, `ACEPTADO`.

### Perfil postulante

- `GESTIONAR_PERFIL_POSTULANTE`
  - Permite completar y actualizar datos del perfil del postulante.
- `GESTIONAR_CV`
  - Permite gestionar CV guardado del postulante.

### Catalogos y administracion

- `ABM_TIPO_AVISO`
  - Alta, baja y modificacion de `TipoAviso`.
- `ABM_SUBTIPO_AVISO`
  - Alta, baja y modificacion de `SubTipoAviso`.
- `ABM_ESTADO_AVISO`
  - Administracion de catalogo de estados de aviso.
- `ABM_ESTADO_POSTULACION`
  - Administracion de catalogo de estados de postulacion.
- `ABM_ESTADO_SOLICITUD`
  - Administracion de catalogo de estados de solicitud.
- `ABM_CARRERA`
  - Administracion de carreras.
- `ABM_TIPO_ESTUDIANTE`
  - Administracion de tipos de estudiante.
- `ABM_EMPRESA`
  - Administracion de empresas.

### Reclutadores, conexiones y solicitudes

- `GESTIONAR_SOLICITUD_RECLUTADOR`
  - Permite revisar y resolver altas o solicitudes vinculadas a reclutadores.
- `GESTIONAR_CONEXIONES_EMPRESA_RECLUTADOR`
  - Permite crear o resolver relaciones entre empresas y reclutadores.
- `GESTIONAR_SOLICITUD_ASOCIACION`
  - Permite gestionar solicitudes de asociacion de empresa.

### Reportes

- `VISUALIZAR_REPORTES`
  - Permite acceder a reportes funcionales del sistema.

## Matriz rol -> permisos

### POSTULANTE

- `VER_AVISOS`
- `VER_DETALLE_AVISO`
- `POSTULARSE_AVISO`
- `SUBIR_CV_POSTULACION`
- `CONSULTAR_POSTULACIONES_PROPIAS`
- `CANCELAR_POSTULACION_PROPIA`
- `GESTIONAR_PERFIL_POSTULANTE`
- `GESTIONAR_CV`

### RECLUTADOR

- `CONSULTAR_SOPORTE_AVISO`
- `CREAR_AVISO`
- `EDITAR_AVISO`
- `PUBLICAR_AVISO`
- `PAUSAR_AVISO`
- `REANUDAR_AVISO`
- `CANCELAR_AVISO`
- `CONSULTAR_AVISOS_PROPIOS`
- `CONSULTAR_POSTULACIONES_RECIBIDAS`
- `RESOLVER_POSTULACION`

### ADMINISTRADOR

- `ABM_TIPO_AVISO`
- `ABM_SUBTIPO_AVISO`
- `ABM_ESTADO_AVISO`
- `ABM_ESTADO_POSTULACION`
- `ABM_ESTADO_SOLICITUD`
- `ABM_CARRERA`
- `ABM_TIPO_ESTUDIANTE`
- `ABM_EMPRESA`
- `GESTIONAR_SOLICITUD_RECLUTADOR`
- `GESTIONAR_CONEXIONES_EMPRESA_RECLUTADOR`
- `GESTIONAR_SOLICITUD_ASOCIACION`
- `VISUALIZAR_REPORTES`

## Notas de implementacion

- Para `AvisoController`, hoy la autorizacion esta basada en rol y actor autenticado. Cuando pasemos a permisos, los endpoints deberian pedir permisos concretos como:
  - `VER_AVISOS`
  - `VER_DETALLE_AVISO`
- Para `AvisoReclutadorController`, los endpoints deberian migrar a permisos como:
  - `CREAR_AVISO`
  - `EDITAR_AVISO`
  - `PAUSAR_AVISO`
  - `REANUDAR_AVISO`
  - `CANCELAR_AVISO`
- Para `AvisoSoporteController`, deberian usarse:
  - `CONSULTAR_SOPORTE_AVISO`
  - o permisos de administracion de catalogos segun el caso

## Recomendacion para el subsistema

Para el sistema `PALA`, conviene cargar:

- roles fijos:
  - `Postulante`
  - `Reclutador`
  - `Administrador`
- permisos con estas claves funcionales
- asociaciones `Rol -> Permiso` segun la matriz anterior

De esa forma el JWT puede traer:

- `roles`: nombre visible del rol
- `permisos`: claves concretas como `VER_AVISOS`, `CREAR_AVISO`, etc.
