package hmm.utils;

import com.bmw.hmm.SequenceState;
import com.bmw.hmm.Transition;
import com.bmw.hmm.ViterbiAlgorithm;
import hmm.types.*;

import java.util.*;

import static hmm.utils.Helper.computeDistance;


public class OfflineMapMatching {

    private final HmmProbabilities hmmProbabilities = new HmmProbabilities();

    private List<GpsMeasurement> gpsMeasurements;

    private RoadEdgeIndex roadEdgeIndex = new RoadEdgeIndex();

    private RoadNetwork roadNetwork;

    private double searchRadius;

    private final static Map<GpsMeasurement, Collection<RoadPosition>> candidateMap =
            new HashMap<>();

    private final static Map<Transition<RoadPosition>, Double> routeLengths = new HashMap<>();

    public OfflineMapMatching(List<GpsMeasurement> gpsMeasurements,
                              List<RoadEdge> roadEdges) {
        this.gpsMeasurements = gpsMeasurements;
        for (RoadEdge roadEdge : roadEdges) {
            this.roadEdgeIndex.add(roadEdge);
        }
        this.searchRadius = 50;
        this.roadNetwork = new RoadNetwork(roadEdges);
//        roadEdgeIndex.tree.visualize(600,600).save("target/mytree.png");

        RoadPosition rp11 = new RoadPosition(1, 1.0 / 5.0, 20.0, 10.0);
        RoadPosition rp12 = new RoadPosition(2, 1.0 / 5.0, 60.0, 10.0);
        RoadPosition rp21 = new RoadPosition(1, 2.0 / 5.0, 20.0, 20.0);
        RoadPosition rp22 = new RoadPosition(2, 2.0 / 5.0, 60.0, 20.0);
        RoadPosition rp31 = new RoadPosition(1, 4.0 / 5.0, 20.0, 40.0);
        RoadPosition rp32 = new RoadPosition(2, 4.0 / 5.0, 60.0, 40.0);
        RoadPosition rp33 = new RoadPosition(3, 1.0 / 4.0, 30.0, 50.0);
        RoadPosition rp41 = new RoadPosition(4, 2.0 / 3.0, 20.0, 70.0);
        RoadPosition rp42 = new RoadPosition(5, 2.0 / 3.0, 60.0, 70.0);

        candidateMap.put(gpsMeasurements.get(0), Arrays.asList(rp11, rp12));
        candidateMap.put(gpsMeasurements.get(1), Arrays.asList(rp21, rp22));
        candidateMap.put(gpsMeasurements.get(2), Arrays.asList(rp31, rp32, rp33));
        candidateMap.put(gpsMeasurements.get(3), Arrays.asList(rp41, rp42));

        addRouteLength(rp11, rp21, 10.0);
        addRouteLength(rp11, rp22, 110.0);
        addRouteLength(rp12, rp21, 110.0);
        addRouteLength(rp12, rp22, 10.0);

        addRouteLength(rp21, rp31, 20.0);
        addRouteLength(rp21, rp32, 40.0);
        addRouteLength(rp21, rp33, 80.0);
        addRouteLength(rp22, rp31, 80.0);
        addRouteLength(rp22, rp32, 60.0);
        addRouteLength(rp22, rp33, 20.0);

        addRouteLength(rp31, rp41, 30.0);
        addRouteLength(rp31, rp42, 70.0);
        addRouteLength(rp32, rp41, 30.0);
        addRouteLength(rp32, rp42, 50.0);
        addRouteLength(rp33, rp41, 70.0);
        addRouteLength(rp33, rp42, 30.0);
    }

    private static void addRouteLength(RoadPosition from, RoadPosition to, double routeLength) {
        routeLengths.put(new Transition<RoadPosition>(from, to), routeLength);
    }

    /*
     * For real map matching applications, candidates would be computed using a radius query.
     */
    private Collection<RoadPosition> computeCandidates(GpsMeasurement gpsMeasurement) {
        Collection result = roadEdgeIndex.search(gpsMeasurement, searchRadius);
        return candidateMap.get(gpsMeasurement);
    }

    /*
     * Returns the shortest route length between two road positions.
     */
    private double computeRouteLength(RoadPosition from, RoadPosition to) {
        // 计算道路上两点之间最短的路径长度
        return roadNetwork.computePathDistance(from, to);
        //return routeLengths.get(new Transition<>(from, to));
    }

    private void computeEmissionProbabilities(
            TimeStep<RoadPosition, GpsMeasurement, RoadPath> timeStep) {
        for (RoadPosition candidate : timeStep.candidates) {
            final double distance =
                    computeDistance(candidate.position, timeStep.observation.position);
            timeStep.addEmissionLogProbability(candidate,
                    hmmProbabilities.emissionLogProbability(distance));
        }
    }

    private void computeTransitionProbabilities(
            TimeStep<RoadPosition, GpsMeasurement, RoadPath> prevTimeStep,
            TimeStep<RoadPosition, GpsMeasurement, RoadPath> timeStep) {
        final double linearDistance = computeDistance(prevTimeStep.observation.position,
                timeStep.observation.position);
        final double timeDiff = (timeStep.observation.time.getTime() -
                prevTimeStep.observation.time.getTime()) / 1000.0;

        for (RoadPosition from : prevTimeStep.candidates) {
            for (RoadPosition to : timeStep.candidates) {

                // For real map matching applications, route lengths and road paths would be
                // computed using a router. The most efficient way is to use a single-source
                // multi-target router.
                final double routeLength = computeRouteLength(from, to);
                timeStep.addRoadPath(from, to, new RoadPath(from, to));

                final double transitionLogProbability = hmmProbabilities.transitionLogProbability(
                        routeLength, linearDistance, timeDiff);
                timeStep.addTransitionLogProbability(from, to, transitionLogProbability);
            }
        }
    }

    public List<SequenceState<RoadPosition, GpsMeasurement, RoadPath>> run() {
        ViterbiAlgorithm<RoadPosition, GpsMeasurement, RoadPath> viterbi =
                new ViterbiAlgorithm<>();
        TimeStep<RoadPosition, GpsMeasurement, RoadPath> prevTimeStep = null;

        for (GpsMeasurement gpsMeasurement : gpsMeasurements) {
            final Collection<RoadPosition> candidates = computeCandidates(gpsMeasurement);
            final TimeStep<RoadPosition, GpsMeasurement, RoadPath> timeStep =
                    new TimeStep<>(gpsMeasurement, candidates);

            computeEmissionProbabilities(timeStep);
            if (prevTimeStep == null) {
                viterbi.startWithInitialObservation(timeStep.observation, timeStep.candidates,
                        timeStep.emissionLogProbabilities);
            } else {
                computeTransitionProbabilities(prevTimeStep, timeStep);
                viterbi.nextStep(timeStep.observation, timeStep.candidates,
                        timeStep.emissionLogProbabilities, timeStep.transitionLogProbabilities,
                        timeStep.roadPaths);
            }
            prevTimeStep = timeStep;
        }

        List<SequenceState<RoadPosition, GpsMeasurement, RoadPath>> roadPositions =
                viterbi.computeMostLikelySequence();

        return roadPositions;
    }

}

