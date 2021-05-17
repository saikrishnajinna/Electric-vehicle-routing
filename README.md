# Electric-vehicle-routing
Delivery vehicles have been major contributor of greenhouse gases causing global warming. Which resulted in idea of using battery consuming electric vehicles for delivering products to customer. This gave birth to Electric Vehicle routing problem. Motive is to plan routes minimizing the distance travelled and keeping a track of battery level. When battery is low, we can go to nearest charging station and recharge. 

**Problem**

With a homogeneous fleet of vehicles available at the depot, route planning should be done minimizing distance and considering the following constraints:

Start (with full battery) and end at depot.

Each customer is visited exactly once.

Customer demands in route should not exceed capacity of vehicle.

When charging stations are visited, vehicles are fully charged

Charging stations can be visited multiple times.

Depot is also a charging station

**Solution**

There are three algorithms used. they are,

-Greedy Algorithm

-Tabu Search Algorithm

-Savings Algorithm

Combining these three algorithm an upgraded heuristic approach is generated.
With the basic information of vehicles, customers, charging stations. First distance matrix, savings matrix and consumption matrix are computed.
With greedy approach initial set of routes are generated, which goes are input for tabu search algorithm.
Tabu search optimized initial routes further and minimize the distance. Being iterative process, the best solution is considered optimal out of iterations done. 
charging stations are added to the optimal routes using savings algorithm only when required.
