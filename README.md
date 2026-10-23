# Laboratorio 2.2 - Formulario Resiliente

Proyecto desarrollado en Kotlin con Jetpack Compose para Android API 36.

## Funcionalidades

- Formulario con Nombre, Email y Notificaciones.
- Persistencia del Nombre mediante DataStore.
- Persistencia del Email mediante SavedStateHandle.
- Estado de Notificaciones manejado en ViewModel.
- Interceptación del gesto Atrás mediante BackHandler.
- Indicador visual de guardado en DataStore.
- Arquitectura basada en flujo de datos unidireccional.

## Diferencia entre SavedStateHandle y DataStore

SavedStateHandle permite conservar datos asociados al estado de la interfaz cuando Android destruye y posteriormente recrea una Activity o el proceso. En este proyecto se utiliza para almacenar el Email.

DataStore persiste información directamente en el almacenamiento del dispositivo. Por esta razón, los datos pueden recuperarse incluso después de cerrar completamente la aplicación. En este proyecto se utiliza para almacenar el Nombre.

## Pruebas realizadas

- Rotación del dispositivo.
- Destrucción de Activity con "Don't keep activities".
- Cierre total y reapertura de la aplicación.
- Interceptación del botón o gesto Atrás.
- Persistencia de datos en DataStore.

## Autor

Sebastian Salcedo
