# Evaluation lab - Apache Kafka

## Group number: 19

## Group members

- Mirko Bitetto
- Jaime Fernández Plaza
- Matteo Colella

## Exercise 1

- Number of partitions allowed for TopicA (1, N)
- Number of consumers allowed (1, N)
    - Consumer 1: GroupA
    - Consumer 2: GroupA
    - ...
    - Consumer N: GroupA

### Observations
- The number of partitions should be higher or equal to the number of consumers in the same group. If the number of consumers exceeds the number of partitions, the exceeding ones are useless
- "GroupA" is just an example. we can use any group name we want, as long as it's the same for each consumer.


## Exercise 2

- Number of partitions allowed for TopicA (1, N)
- Number of consumers allowed (1, N)
    - Consumer 1: GroupA
    - Consumer 2: GroupA
    - ...
    - Consumer N: GroupA

### Observations
- The number of partitions should be higher or equal to the number of consumers in the same group. If the number of consumers exceeds the number of partitions, the exceeding ones are useless
- Another implementation could be done by using log compaction in the inputTopic, it is used to store only the last value for each key, which is the one used for average calculation.
- "GroupA" is just an example. we can use any group name we want, as long as it's the same for each consumer.
