# Guía para que GioMonteggia resuelva "Add as a collaborator"

Sigue estos pasos para agregar a tu compañero como colaborador de tu repositorio
y que pueda abrir un PR hacia main de TU repo (como pide el taller).

## 1) Agregalo como colaborador en GitHub

En tu navegador:
1. Entrá a https://github.com/luckvani-gif/vricciardi-tallerlp3-2026/settings/access
2. Apretá "Collaborators" → "Add people"
3. Buscá `GioMonteggia` y agregalo con permiso "Write" (o "Maintain")
4. Él tiene que aceptar la invitación que le llega por mail/notification.

## 2) Clonar tu repo (desde su máquina/terminal)

Él debe hacer esto desde su git bash/terminal (no desde OpenCode):

```bash
cd ~  # o donde quiera trabajar
git clone https://github.com/luckvani-gif/vricciardi-tallerlp3-2026.git
cd vricciardi-tallerlp3-2026
```

## 3) Crear rama con sus iniciales

```bash
git checkout -b GIO-contribucion-lp3
```

## 4) Agregar UNA especialización que no pise archivos existentes

Que agregue SOLO archivos nuevos. Ejemplo: una clase que herede de alguna existente,
su propio controller (si quiere exponerlo aparte), y sus tests. NO toquen README,
NO toquen LICENSE, NO renombren paquetes, NO borren archivos.

```bash
# crear archivos nuevos...
git status
git add src/main/java/.../Nuevo.java src/test/java/.../NuevoTest.java  # solo lo nuevo
git commit -m "feat(cs2): agrega [NombreEspecializacion] sin modificar archivos existentes"
git push -u origin GIO-contribucion-lp3
```

## 5) Abrir Pull Request hacia TU main

Desde GitHub:
- Abrir https://github.com/luckvani-gif/vricciardi-tallerlp3-2026/compare/main...GIO-contribucion-lp3
- Completar con la plantilla del taller (Qué se agrega, Cómo probar, sin pisar README)
- Crear Pull Request

## 6) Revisar y mergear (vos)

Como dueño, revisá que:
- Solo haya archivos nuevos (`A` en diff)
- Compile: `./mvnw compile` y `./mvnw test` pasan
- No modifique archivos tuyos
- Cumpla lo que pide el enunciado

Luego mergeá el PR (Squash & merge o Create merge commit, cualquiera sirve).

## Notas importantes
- NO deben reescribir historia ya pusheada.
- NO deben tocar tu README ni BITACORA.md.
- Todo nuevo, en archivos nuevos.
- Tiene que compilar y arrancar con `./mvnw spring-boot:run`.
