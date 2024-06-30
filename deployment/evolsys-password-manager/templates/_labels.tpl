{{- define "app.labels" -}}
{{ include "app.match-labels" . }}
app: "{{ .Release.Name }}"
helm.sh/chart: {{ include "app.chart" . }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{- define "app.match-labels" -}}
app.kubernetes.io/name: {{ include "app.name" . }}
app.kubernetes.io/instance: {{ printf "%s-%s" .Release.Name .Values.global.app }}
{{- end -}}

