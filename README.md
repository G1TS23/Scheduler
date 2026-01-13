# TDD Scheduler

Individuellement ou en binôme, en adoptant une approche TDD, vous élaborez une classe déclenchant des tâches planifiées.

> [!warning]
> L'absence de commit git pour chacune des 3 étapes (Red, green, refactor) de la majorité des itérations TDD de l'atelier rendra l'atelier hors-sujet (oublis ponctuels tolérés).


Coder progressivement, test après test, une classe Scheduler qui gère l'exécution de tâches planifiées. Pour cela, cette classe possèdera A TERME les membres suivants :

- un accesseur énumérant les tâches actuellement planifiées,
- une méthode de définition/modification (setTask dans l'exemple) d'une tâche planifiée caractérisée par un nom, un périodicité (voir plus bas) et une interface ou une lambda donnée (aka fonction anonyme, callback, ...),
- une méthode de suppression d'une tâche planifiée par son nom,
- une méthode d'activation périodique sans paramètre (update dans l'exemple) qui lance une par une les tâches qui doivent l'être à l'heure actuelle.

Exemple où EveryDay n'est pas forcément le meilleur moyen de définir une périodicité :

```bash
var sch = new Scheduler(new MySystemClock());

sch.setTask("backup", new EveryDayAt(3, 00), () -> Obj.doBackup());
for(;;) {
sch.update();
Thread.sleep(1000);
}
```
Choisir une par une les périodicités à gérer parmi cette liste des syntaxes de la table cron (mais pas forcément sous forme de chaîne de caractères) par ordre de difficulté croissante. Il est demandé d'en gérer autant que possible.

Si votre implémentation l'exige, il est possible de coder des classes supplémentaires transformant certains tests unitaires en tests d'intégration.

En dehors de ces classes, afin de disposer de tests déterministes et génériques, les autres dépendances de Scheduler doivent, bien entendu, être simulées et notamment le temps et l'interface/lambda.