# MODELOS

- 1. ALIMENTO  
| ATRIBUTO | TIPO | OBLIGATORIO | DERIVADA |
| --- | --- | --- | --- |
| Nombre | String | Si | No |
| Calorías | Double | Si | No |
| Categoría | Enumerado (Carne, Pescado, etc) | Si | No |
| Tipo | Enumerado (Grasa, Proteina, HdC, SalMin, ViT) | Si | No |
| Alergeno | Enumerado (Lechita) | Si | No |
| Temporada | Enumerado<Estaciones> | No | No | 

- 2. PLATO  
| ATRIBUTO | TIPO | OBLIGATORIO | DERIVADA |
| --- | --- | --- | --- |
| ListaAlergenos | List<Enumerado> | Si | No |
| Alimentos | Mapa<Alimento: Cantidad (gramos)> | Si | No |
| Nombre | String | Si | No |
| Calorias | Double | Si | Si |
| ProvinciaOrigen | String | No | No |
| CAOrigen | String | Si | No |
| Temporada | Enumerado<Estaciones> | No | No | 

- 3. DIETA  
| ATRIBUTO | TIPO | OBLIGATORIO | DERIVADA |
| --- | --- | --- | --- |
| Nombre | String | Si | No |
| Platos | List<Plato> | Si | No |
| Descripción | String | No | No |
| TipoDieta | Enumerado<(Veganos, Vegetarianos, Diabetico)> | No | No |

# ENDPOINTS
 - GET
 * CONSULTAR DE TODOS LOS MODELOS
 * LISTA DE TODOS LOS ALERGENOS
 * DAR ALGO (POR NOMBRE) + QUERY (FILTRAR QUE QUIERES Y NO VER)
 * SACAR PLATOS O ALIMENTOS POR TEMPORADA / C.A. / TIPO DE DIETA
 - POST
 - PUT
 - DELETE
