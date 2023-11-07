# Integrantes

- Bultri, Juan Eliseo (juanbultri)
- D`Amico Tomás (tomidamico)
- De Robles Fluck, Felipe (flvckz)
- Kupersmit, Pedro (pedrokuper)
- Lerner, Felix (flerner)
- Moragues Beckar, Agustín (amoraguesbeckar)

1. En el caso que se pida extender la app para otros tipos de mascotas, por ejemplo gatos, ¿la app es flexible? ¿Qué cambios realizarían?
   Identificamos que podríamos haber usado una clase abstracta para contener a la mascota en la entidad PublicationEntity por ej "Pet" y que cada mascota herede de esa clase. De esa forma en caso
   de extenderse la app sería más sostenible agregar a distintos animales. 

3. ¿Qué tipo de arquitectura usaron y por qué? ¿Podría mejorarla?
A nuestro entender, realizamos una suerte de MVC, donde los .xml y fragments son nuestro View, nuestros servicios (que acceden hacia la db a través de Firebase) podrian ser el equivalente a Controller y nuestro dominios y entities son el Model.
Podriamos mejorarla adoptando MVVM, mediante el uso de View Models.

4. ¿Qué mejoras detectan que podrían realizarle a la app?
Notamos que podríamos haber usado un patrón decorator  para la realización de filtros. Es un caso ideal ya que son comportamiento que se van anidando con otros comportamientos. Otra oportunidad de mejora sería haber usado viewmodel para referenciar a los componentes.
Algunas funcionalidades podrian haberse abstraido en clases y reutilizado como métodos con parámetros y callbacks.

