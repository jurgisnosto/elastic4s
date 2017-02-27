package com.sksamuel.elastic4s.searches.aggs

import com.sksamuel.elastic4s.ScriptBuilder
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder

import scala.collection.JavaConverters._

object RangeAggregationBuilder {

  def apply(agg: RangeAggregationDefinition): RangeAggregationBuilder = {

    val builder = AggregationBuilders.range(agg.name)

    agg.field.foreach(builder.field)
    agg.missing.foreach(builder.missing)
    agg.format.foreach(builder.format)
    agg.keyed.foreach(builder.keyed)
    agg.script.map(ScriptBuilder.apply).foreach(builder.script)

    agg.ranges.foreach {
      case (Some(key), from, to) => builder.addRange(key, from, to)
      case (None, from, to) => builder.addRange(from, to)
    }

    agg.unboundedFrom.foreach {
      case (Some(key), from) => builder.addUnboundedFrom(key, from)
      case (None, from) => builder.addUnboundedFrom(from)
    }

    agg.unboundedTo.foreach {
      case (Some(key), to) => builder.addUnboundedTo(key, to)
      case (None, to) => builder.addUnboundedTo(to)
    }

    agg.subaggs.map(AggregationBuilder.apply).foreach(builder.subAggregation)
    // todo avg.pipelines.map(AggregationBuilder.apply).foreach(builder.subAggregation)
    if (agg.metadata.nonEmpty) builder.setMetaData(agg.metadata.asJava)
    builder
  }
}
